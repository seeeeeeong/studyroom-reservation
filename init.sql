CREATE EXTENSION IF NOT EXISTS btree_gist;

SET timezone = 'UTC';

-- 초기화 완료 메시지
SELECT 'PostgreSQL 데이터베이스 초기화 완료!' as message;
