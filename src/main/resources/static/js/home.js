// Mobile menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const menuBtn = document.querySelector('.mobile-menu-btn');
    const mobileMenu = document.querySelector('.mobile-menu');

    if (menuBtn && mobileMenu) {
        menuBtn.addEventListener('click', function() {
            if (mobileMenu.style.display === 'none' || mobileMenu.style.display === '') {
                mobileMenu.style.display = 'block';
                mobileMenu.style.animation = 'fadeInUp 0.3s ease-out';
            } else {
                mobileMenu.style.display = 'none';
            }
        });
    }

    // Scroll reveal animation for elements
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -100px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.animation = 'revealOnScroll 0.8s ease-out forwards';
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observe CTA section elements
    const ctaSection = document.querySelectorAll('.cta-heading, .cta-description, .cta-buttons');
    ctaSection.forEach(el => {
        if (el) {
            el.style.opacity = '0';
            observer.observe(el);
        }
    });

    // Parallax effect on scroll for hero shapes
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        const shapes = document.querySelectorAll('.fluid-shape');

        shapes.forEach((shape, index) => {
            const speed = 0.5 + (index * 0.2);
            shape.style.transform = `translateY(${scrolled * speed}px)`;
        });
    });

    // Number counter animation for stats
    const stats = document.querySelectorAll('.stat-item > div:first-child');

    stats.forEach(stat => {
        const target = parseInt(stat.textContent);
        if (isNaN(target)) return;

        let current = 0;
        const increment = target / 50;
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                stat.textContent = target;
                clearInterval(timer);
            } else {
                stat.textContent = Math.floor(current);
            }
        }, 30);
    });

    // Button hover transition tweak
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary, .register-btn');
    buttons.forEach(btn => {
        btn.addEventListener('mouseenter', function() {
            this.style.transition = 'all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55)';
        });
    });

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Stagger animation to footer links
    const footerLinks = document.querySelectorAll('.footer-link');
    footerLinks.forEach((link, index) => {
        link.style.opacity = '0';
        link.style.animation = `fadeInUp 0.5s ease-out ${index * 0.05}s forwards`;
    });
});
