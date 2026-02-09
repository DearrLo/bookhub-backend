USE BOOKHUB;

DELETE
FROM bookhub_loan;
DELETE
FROM reservation;
DELETE
FROM review;
DELETE
FROM book;
DELETE
FROM bookhub_user;
DELETE
FROM category;

INSERT INTO category (label)
VALUES ('Jeunesse'),
       ('Classique'),
       ('Théâtre'),
       ('Aventure'),
       ('Biographie'),
       ('Science-Fiction');

INSERT INTO book (isbn, title, author, category_id, stock, summary, cover_image_url, created_at)
VALUES ('9782070408504', 'Le Petit Prince', 'Antoine de Saint-Exupéry', 1, 10,
        'Un aviateur tombe dans le désert et rencontre un petit garçon venu d''une autre planète.',
        'https://covers.openlibrary.org/b/isbn/9780156012195-L.jpg', GETDATE()),

       ('9780451525260', 'Les Misérables', 'Victor Hugo', 2, 3,
        'Jean Valjean cherche la rédemption dans une France du XIXe siècle marquée par la misère.',
        'https://covers.openlibrary.org/b/isbn/9780451525260-L.jpg', GETDATE()),

       ('9782253004059', 'Madame Bovary', 'Gustave Flaubert', 2, 4,
        'Emma Bovary cherche à fuir l''ennui de sa vie de province dans des rêves de luxe et de passion.',
        'https://covers.openlibrary.org/b/isbn/9780553213416-L.jpg', GETDATE()),

       ('9782290000519', 'Cyrano de Bergerac', 'Edmond Rostand', 3, 6,
        'Un poète au grand nez aide un ami à séduire la belle Roxane en lui soufflant ses propres vers.',
        'https://covers.openlibrary.org/b/isbn/9782290000519-L.jpg', GETDATE()),

       ('9782253000556', 'Germinal', 'Émile Zola', 2, 5,
        'La révolte des mineurs du Nord face à la faim et l''injustice sociale au XIXe siècle.',
        'https://covers.openlibrary.org/b/isbn/9782253000556-L.jpg', GETDATE()),

       ('9782253005438', 'Bel-Ami', 'Guy de Maupassant', 2, 4,
        'Georges Duroy utilise ses charmes pour gravir les échelons de la haute société parisienne.',
        'https://covers.openlibrary.org/b/isbn/9782253005438-L.jpg', GETDATE()),

       ('9782253013327', 'Le Tour du Monde en 80 Jours', 'Jules Verne', 4, 7,
        'Le défi fou de Phileas Fogg : faire le tour du globe en moins de 80 jours.',
        'https://covers.openlibrary.org/b/isbn/9780613092180-L.jpg', GETDATE()),

       ('9782070367962', 'Mémoires d''une jeune fille rangée', 'Simone de Beauvoir', 5, 2,
        'Le récit de l''émancipation intellectuelle d''une figure majeure du féminisme.',
        'https://covers.openlibrary.org/b/isbn/9782070367962-L.jpg', GETDATE()),

       ('9782710300250', 'Antigone', 'Jean Anouilh', 3, 8,
        'La révolte solitaire d''une jeune femme contre la loi inflexible du roi Créon.',
        'https://covers.openlibrary.org/b/isbn/9782710300250-L.jpg', GETDATE()),

       ('9782266205412', 'La Planète des singes', 'Pierre Boulle', 6, 5,
        'Des voyageurs spatiaux découvrent une planète où les singes dominent les hommes.',
        'https://covers.openlibrary.org/b/isbn/9782266205412-L.jpg', GETDATE()),

       ('9780451524935', '1984', 'George Orwell', 6, 12,
        'Dans un monde totalitaire, Winston Smith tente de résister à l''oppression de Big Brother.',
        'https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg', GETDATE());

INSERT INTO bookhub_user(ROLE, NAME, PSEUDONYM, SURNAME, EMAIL, PASSWORD)
VALUES ('lecteur', 'Jean', 'jeannot31', 'André', 'jeannot31@email.com', 'Pompompom1!'),
       ('admin', 'Nour', 'nourette', 'Martin', 'nour567@email.com', 'Pompompom2!'),
       ('bibliothecaire', 'Soléna', 'soso', 'Toussaint', 'solena@email.com', 'Pompompom3!');

INSERT INTO reservation(BOOK_ID, REQUEST_DATE, RESERVATION_STATUS, USER_ID)
VALUES (1, GETDATE(), 'EN_ATTENTE', 'jeannot31@email.com'),
       (4, GETDATE(), 'EN_ATTENTE', 'jeannot31@email.com'),
       (10, GETDATE(), 'EN_ATTENTE', 'jeannot31@email.com');

INSERT INTO bookhub_loan(book_id, date_to_return, returned_date, loan_date, loan_status, user_id)
VALUES (3, GETDATE() + 14, null, GETDATE(), 'EMPRUNTE', 'jeannot31@email.com'),
       (7, GETDATE() + 14, null, GETDATE(), 'EMPRUNTE', 'jeannot31@email.com'),
       (8, GETDATE(), GETDATE(), GETDATE() - 14, 'RENDU', 'jeannot31@email.com');

INSERT INTO review(book_id, rating, created_at, commentary, user_id)
VALUES (1, 4, GETDATE() - 5, 'Pas mal', 'jeannot31@email.com'),
       (2, 5, GETDATE() - 5, 'Génial', 'jeannot31@email.com'),
       (3, 1, GETDATE() - 5, 'Pas trop aimé', 'jeannot31@email.com');


SELECT *
FROM book;

SELECT *
FROM category;

SELECT *
FROM bookhub_user;

SELECT *
FROM reservation;

SELECT *
FROM bookhub_loan;

SELECT *
FROM review;

