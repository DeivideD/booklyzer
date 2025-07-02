-- V2__Create_Tables.sql --
INSERT INTO public.authors (id, name) VALUES
('8b0162f2-0e2f-49e5-89ea-0a94bc723fc1', 'George Orwell'),
('5f233b56-9c44-4c16-bc09-dc2b7cfeb2c9', 'Jane Austen'),
('700315a6-f1a7-4127-aefb-5e96e6d3656a', 'J.K. Rowling'),
('9e663f7d-88f9-4206-baba-c2e71dce5bbf', 'Mark Twain'),
('0a707235-8c50-4873-9e79-d7462bd28914', 'Ernest Hemingway'),
('674c8dc0-9cb4-41c0-bf9b-b96c405c5f42', 'Leo Tolstoy'),
('fae49b94-f4c5-40db-b5a5-b15703878ab9', 'Agatha Christie'),
('c0a8c52e-8ef9-4a9c-b4ec-5a4904d6d9b0', 'Isaac Asimov'),
('775c5e3e-6eaf-4e54-850e-b365bd387498', 'Stephen King'),
('0ecfc4a9-04c3-43c0-a08e-969e9856ab1a', 'Gabriel García Márquez');

INSERT INTO public.books (id, available_quantity, isbn, title, author_id) VALUES
('1ae7b2b2-c5ee-41e0-a3c7-b0cd6a06f47b', 5, '9780451524935', '1984', '8b0162f2-0e2f-49e5-89ea-0a94bc723fc1'),
('f0a2a8bb-8451-417f-8ec0-4e3d22b06c8d', 3, '9780141439518', 'Pride and Prejudice', '5f233b56-9c44-4c16-bc09-dc2b7cfeb2c9'),
('208d3aa4-9830-4719-9ac0-fc7eb4be9de4', 7, '9780747532743', 'Harry Potter and the Philosopher`s Stone', '700315a6-f1a7-4127-aefb-5e96e6d3656a'),
('a2b2c3d4-5678-49ab-9101-123456789abc', 2, '9780486280615', 'Adventures of Huckleberry Finn', '9e663f7d-88f9-4206-baba-c2e71dce5bbf'),
('56cd2307-1b7d-4534-8e1b-260c5f929eda', 4, '9780684801223', 'The Old Man and the Sea', '0a707235-8c50-4873-9e79-d7462bd28914'),
('3f6a5f15-eced-4ff1-b70f-9e943e0dc6b6', 6, '9780199232765', 'War and Peace', '674c8dc0-9cb4-41c0-bf9b-b96c405c5f42'),
('eec79b94-d7c0-4781-949e-e9389eac496a', 3, '9780062073488', 'Murder on the Orient Express', 'fae49b94-f4c5-40db-b5a5-b15703878ab9'),
('12d88ad2-964c-443e-8806-3794d3de66d2', 8, '9780553294385', 'Foundation', 'c0a8c52e-8ef9-4a9c-b4ec-5a4904d6d9b0'),
('40be7b25-3eb1-4c4a-a5a0-441e1e69e7d4', 1, '9781501142970', 'The Shining', '775c5e3e-6eaf-4e54-850e-b365bd387498'),
('9a20103e-89e9-4e5f-8373-66b4d1091352', 5, '9780060883287', 'One Hundred Years of Solitude', '0ecfc4a9-04c3-43c0-a08e-969e9856ab1a');


INSERT INTO public.users (id, email, name, password, registration) VALUES
('1a2b3c4d-1111-4aa1-8890-fb54a431a111', 'alice@example.com', 'Alice Johnson', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG001'),
('1a2b3c4d-2222-4aa2-8890-fb54a431a222', 'bob@example.com', 'Bob Smith', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG002'),
('1a2b3c4d-3333-4aa3-8890-fb54a431a333', 'carla@example.com', 'Carla Mendes', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG003'),
('1a2b3c4d-4444-4aa4-8890-fb54a431a444', 'daniel@example.com', 'Daniel Souza', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG004'),
('1a2b3c4d-5555-4aa5-8890-fb54a431a555', 'emily@example.com', 'Emily Davis', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG005'),
('1a2b3c4d-6666-4aa6-8890-fb54a431a666', 'fabio@example.com', 'Fábio Lima', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG006'),
('1a2b3c4d-7777-4aa7-8890-fb54a431a777', 'giovanna@example.com', 'Giovanna Rocha', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG007'),
('1a2b3c4d-8888-4aa8-8890-fb54a431a888', 'henrique@example.com', 'Henrique Silva', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG008'),
('1a2b3c4d-9999-4aa9-8890-fb54a431a999', 'isabela@example.com', 'Isabela Monteiro', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG009'),
('1a2b3c4d-aaaa-4aaa-8890-fb54a431aaaa', 'joao@example.com', 'João Pedro', '$2a$10$hBO/l.V8y.yGQQaSFfwSFOVxO0XNtCNbE/Xl4oo7Vn.rCkoxjdJxO', 'REG010');

