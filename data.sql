ALTER TABLE reservations
    ADD CONSTRAINT reservations_no_overlap
    EXCLUDE USING gist (room_id WITH =, tstrange(start_at, end_at, '[)') WITH &&);
