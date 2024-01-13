DO
$$
    DECLARE
        user_id_1         uuid      := '73162244-c3f4-4682-ada1-67bfa54838fe'::uuid;
        user_id_2         uuid      := '73162245-c3f4-4682-ada1-67bfa54838fe'::uuid;

        account_id_1         uuid      := '73162246-c3f4-4682-ada1-67bfa54838fe'::uuid;
        account_id_2         uuid      := '73162247-c3f4-4682-ada1-67bfa54838fe'::uuid;

    BEGIN
        INSERT INTO bank_user (user_id, first_name, last_name, phone_number)
        VALUES (user_id_1, 'John', 'Doe', '123123123'),
        (user_id_2, 'Jane', 'Doe', '456456456');

        INSERT INTO bank_account (account_id, user_id, account_name, account_number, cvv, expires_at, balance, version)
        VALUES (account_id_1, user_id_1, 'ImportedForJohn', 'PL12345678901234567890123456', '123', '2027-01-05 00:00:00'::timestamp, 1000, 1),
               (account_id_2, user_id_2, 'ImportedForJane', 'PL09876543210987654321098765', '321', '2027-01-05 00:00:00'::timestamp, 1000, 1);

    END
$$;