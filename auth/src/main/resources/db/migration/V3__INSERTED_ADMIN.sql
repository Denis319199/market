INSERT INTO users(username, password, role, is_enabled, first_name, last_name, patronymic, phone_number, country_id)
VALUES ('admin', '$2a$12$7XXlt.w7DcNK9lU2jXAasuSgM8KkW11Ocsp/5V3ptC0qSZgCwIILu', 0, true, 'admin', 'admin', null,
        1000000000, 1)
ON CONFLICT DO NOTHING;