document.addEventListener("DOMContentLoaded", function() {
    let profileDropdown = document.querySelector('.profile-dropdown');
    let profileHeaderBtn = document.querySelector('.account-navbar-link');

    let dropdown = false;

    profileHeaderBtn.addEventListener('mouseover', function() {
        if (!dropdown) {
            dropdown = true;
            displayDropdown(profileDropdown, true);
        }
    });

    profileHeaderBtn.addEventListener('mouseout', function() {
        if (dropdown) {
            dropdown = false;
            setTimeout(() => displayDropdown(profileDropdown, dropdown), 200);
        }
    });
});

function displayDropdown (profileDropdown, showBar) {
    if (showBar) {
        profileDropdown.classList.add('active');
    } else {
        profileDropdown.classList.remove('active');
    }
}