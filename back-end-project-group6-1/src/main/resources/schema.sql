DROP TABLE IF EXISTS recipes CASCADE;
DROP TABLE IF EXISTS cookbooks CASCADE;
DROP TABLE IF EXISTS family_members CASCADE;
DROP TABLE IF EXISTS family CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS ingredients CASCADE;
DROP TABLE IF EXISTS profile CASCADE;
DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS recipe_ingredients CASCADE;


CREATE TABLE "user" (
                        id BIGSERIAL NOT NULL,
                        created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP(3) NOT NULL,
                        username TEXT NOT NULL,
                        first_name TEXT NOT NULL,
                        last_name TEXT NOT NULL,
                        email TEXT NOT NULL,
                        password TEXT NOT NULL,
                        role TEXT NOT NULL,
                        CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE family (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(250) NOT NULL,
                        head_of_family_id BIGINT NOT NULL,
                        CONSTRAINT fk_head_of_family
                            FOREIGN KEY (head_of_family_id)
                                REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE family_members (
                                family_id BIGINT NOT NULL,
                                user_id   BIGINT NOT NULL,
                                PRIMARY KEY (family_id, user_id),
                                CONSTRAINT fk_fm_family
                                    FOREIGN KEY (family_id)
                                        REFERENCES family(id) ON DELETE CASCADE,
                                CONSTRAINT fk_fm_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE cookbooks (
                           ID SERIAL PRIMARY KEY,
                           TITLE VARCHAR(250),
                           NUM_OF_RECIPES INT,
                           user_id BIGINT,
                           family_id BIGINT,
                           FOREIGN KEY (user_id) REFERENCES "user"(id),
                           FOREIGN KEY (family_id) REFERENCES family(id) ON DELETE SET NULL
);

CREATE TABLE recipes (
                         ID SERIAL PRIMARY KEY,
                         TITLE VARCHAR(250),
                         PREP_TIME INT,
                         COOK_TIME INT,
                         cookbook_id INT NOT NULL,
                         FOREIGN KEY (cookbook_id) REFERENCES cookbooks(id)
);

CREATE TABLE ingredients (
                             ID SERIAL PRIMARY KEY,
                             NAME VARCHAR(50)
);
CREATE TABLE profile (
                         id SERIAL PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         about TEXT,
                         user_id BIGINT UNIQUE NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);
CREATE TABLE recipe_ingredient (
                                   recipe_id BIGINT NOT NULL,
                                   ingredient_id BIGINT NOT NULL,
                                   PRIMARY KEY (recipe_id, ingredient_id),
                                   CONSTRAINT fk_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE );
CREATE TABLE IF NOT EXISTS password_reset_tokens (
                                                     id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     token      VARCHAR(255) NOT NULL UNIQUE,
    user_id    BIGINT       NOT NULL,
    expiry_date TIMESTAMP   NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_reset_user FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
    );