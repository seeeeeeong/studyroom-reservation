# Study Room Reservation System

PostgreSQL의 **EXCLUDE USING GIST 제약조건**을 활용하여 예약 충돌을 데이터베이스 레벨에서 방지합니다.

## 기술 아키텍처

### 동시성 제어 방식 비교

#### 1. **PostgreSQL EXCLUDE USING GIST** (채택)
```sql
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    EXCLUDE USING GIST (
        room_id WITH =,
        tstrange(start_at, end_at) WITH &&
    )
);
```

**장점:**
- **데이터베이스 레벨 보장**: 애플리케이션 로직과 무관하게 충돌 방지
- **높은 성능**: 인덱스 기반으로 빠른 충돌 검사
- **확장성**: 다중 인스턴스 환경에서도 일관성 보장
- **안정성**: Race Condition 완전 차단

**동작 원리:**
- `tstrange(start_at, end_at)`: 시간 범위 생성
- `WITH &&`: 겹침(overlap) 연산자
- `room_id WITH =`: 같은 방에서만 충돌 검사

#### 2. 트랜잭션 + 행잠금/낙관락 (대안)

**행잠금 방식:**
```java
@Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId " +
       "AND r.startAt < :endAt AND r.endAt > :startAt FOR UPDATE")
List<Reservation> findConflictingReservations(Long roomId, LocalDateTime startAt, LocalDateTime endAt);
```

**낙관락 방식:**
```java
@Entity
public class Reservation {
    @Version
    private Long version;
    // ...
}
```

## 데이터베이스 스키마

```sql
-- PostgreSQL btree_gist 확장 활성화
CREATE EXTENSION IF NOT EXISTS btree_gist;

-- 방 테이블
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    capacity INTEGER NOT NULL
);

-- 예약 테이블 (GIST 제약조건 포함)
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES rooms(id),
    user_id VARCHAR(255) NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    EXCLUDE USING GIST (
        room_id WITH =,
        tsrange(start_at, end_at) WITH &&
    )
);
```








## 동시성 테스트

같은 시간대에 10개의 병렬 요청

```java
// 동시 예약 시도
@Test
void 동시_예약_충돌_테스트() {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    List<Future<Boolean>> futures = new ArrayList<>();
    
    for (int i = 0; i < 10; i++) {
        futures.add(executor.submit(() -> {
            try {
                reservationService.createReservation(request, "user" + i);
                return true;
            } catch (StudyroomException e) {
                return false;
            }
        }));
    }
    
    // 정확히 1개만 성공해야 함
    long successCount = futures.stream()
        .mapToBoolean(this::getFutureResult)
        .filter(success -> success)
        .count();
    
    assertThat(successCount).isEqualTo(1);
}
```

**결과:** 정확히 1건만 성공, 나머지 9건은 `RESERVATION_TIME_CONFLICT` 예외
