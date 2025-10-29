document.addEventListener("DOMContentLoaded", () => {
    const alerts = document.querySelectorAll(".alert");

    // Fade in alerts
    alerts.forEach(alert => {
        alert.style.opacity = 0;
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s";
            alert.style.opacity = 1;
        }, 100);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(() => {
        alerts.forEach(alert => {
            alert.style.transition = "opacity 0.5s";
            alert.style.opacity = 0;
            setTimeout(() => alert.remove(), 500);
        });
    }, 5000);
});
