DO
$$
    DECLARE
        user_id_1         uuid      := '73162244-c3f4-4682-ada1-67bfa54838fe'::uuid;
        user_id_2         uuid      := '73162245-c3f4-4682-ada1-67bfa54838fe'::uuid;

        account_id_1         uuid      := '73162246-c3f4-4682-ada1-67bfa54838fe'::uuid;
        account_id_2         uuid      := '73162247-c3f4-4682-ada1-67bfa54838fe'::uuid;

    BEGIN
        INSERT INTO bank_user (user_id, first_name, last_name, phone_number, iv)
        VALUES (user_id_1, 'Ih/UZ5tRXbmbd0ujg5V3aQ==', '/lQFNhDcDsCUoKlVdShM/Q==', 'OjEOYJVDGlDvCUJ7izv8MQ==', E'\\x92865D2160D3EFC788867DCBC437B99F'),
        (user_id_2, 'oG6glxRkoIYzQtfYSGPiuQ==', 'pBubAdrESjcskqUZ5LIlNw==', 'eVBkxJ8y9fV3yBfjQju8rA==', E'\\x02D7DF6F8915C47A1AB67C71E113208B');

        INSERT INTO bank_account (account_id, user_id, account_name, account_number, cvv, expires_at, balance, version, iv)
        VALUES (account_id_1, user_id_1, 'ImportedForJohn', 'PL12345678901234567890123456', 'bgmylOOjuOlmADinIe7OKA==', '2027-01-05 00:00:00'::timestamp, '+AEyVFk1Lphy6Hv7QvXkfw==', 1, E'\\x749AA933711986B49C7D2A0BECB006C1'),
               (account_id_2, user_id_2, 'ImportedForJane', 'PL09876543210987654321098765', 'DvRq/fTqxg3I7/GebRkbXQ==', '2027-01-05 00:00:00'::timestamp, 'CwgfwhUH/jV1/a+aJRZrag==', 1, E'\\x338D3F2BC5A7EE3E19349AD5475EFAE6');

    END
$$;