function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const navbar = document.querySelector('.navbar');
    const actionSection = document.querySelector('.action-section');
    const content = document.querySelector('.content');

    sidebar.classList.toggle('hidden');
    actionSection.classList.toggle('collapsed');
    content.classList.toggle('collapsed');
}