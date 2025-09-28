CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE OR REPLACE FUNCTION add_reservation_constraint() 
RETURNS VOID AS $
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'reservations') THEN
BEGIN
ALTER TABLE reservations
    ADD CONSTRAINT reservations_no_overlap
    EXCLUDE USING gist (room_id WITH =, tstzrange(start_at, end_at, '[)') WITH &&);
EXCEPTION
            WHEN duplicate_object THEN NULL;
END;
END IF;
END;
$ LANGUAGE plpgsql;
