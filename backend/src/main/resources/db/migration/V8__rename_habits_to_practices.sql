-- Rename table habits -> practices for existing production databases
-- where V1-V7 have already been applied with the old naming.

ALTER SEQUENCE habits_id_seq RENAME TO practices_id_seq;

ALTER TABLE check_ins DROP CONSTRAINT check_ins_habit_id_fkey;

ALTER TABLE habits RENAME TO practices;

ALTER TABLE check_ins RENAME COLUMN habit_id TO practice_id;

ALTER TABLE check_ins ADD CONSTRAINT check_ins_practice_id_fkey
    FOREIGN KEY (practice_id) REFERENCES practices(id) ON DELETE CASCADE;
