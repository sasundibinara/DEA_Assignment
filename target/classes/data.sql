INSERT INTO users (id, email, full_name, password, role, enabled)
SELECT 1, 'admin@alpms.local', 'Admin', '$2a$10$8Wlq0z0u3m8D7vQk9z7vwe6wrM1xC1Q7O2tZ0q9Qw0j8N1QYdGm6S', 'ADMIN', true
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@alpms.local');