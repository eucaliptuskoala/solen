ALTER TABLE check_ins DROP CONSTRAINT check_ins_practice_id_fkey;
ALTER TABLE check_ins ADD CONSTRAINT check_ins_practice_id_fkey
    FOREIGN KEY (practice_id) REFERENCES practices(id) ON DELETE CASCADE;
