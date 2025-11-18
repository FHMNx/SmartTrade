window.addEventListener("load", async () => {
    Notiflix.Loading.pulse("Data is loading", {
        clickToClose: false,
        svgColor: '#0284c7'
    });
    try {
        await getCities();
        await loadUserData();
    } finally {
        Notiflix.Loading.remove();
    }
});

document.getElementById("address-anchor").addEventListener("click", async () => {
    await loadAddress();
});

async function loadAddress() {
    Notiflix.Loading.pulse("wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch("api/profiles/addresses");
        if (response.ok) {
            const data = await response.json();
            document.getElementById("addName").innerHTML = `Name: ${data.name}`;
            document.getElementById("addEmail").innerHTML = `Email: ${data.email}`;
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove();
    }
}

async function saveChanges() {
    Notiflix.Loading.pulse("wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    let firstName = document.getElementById("firstName");
    let lastName = document.getElementById("lastName");
    let lineOne = document.getElementById("lineOne");
    let lineTwo = document.getElementById("lineTwo");
    let postalCode = document.getElementById("postalCode");
    let citySelect = document.getElementById("citySelect");
    let mobile = document.getElementById("mobile");
    let currentPassword = document.getElementById("currentPassword");
    let newPassword = document.getElementById("newPassword");
    let confirmPassword = document.getElementById("confirmPassword");

    const userObj = {
        firstName: firstName.value,
        lastName: lastName.value,
        lineOne: lineOne.value,
        lineTwo: lineTwo.value,
        postalCode: postalCode.value,
        cityId: citySelect.value,
        mobile: mobile.value,
        password: currentPassword.value,
        newPassword: newPassword.value,
        confirmPassword: confirmPassword.value,
    };

    try {
        const response = await fetch("api/profiles/update-profile", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userObj)
        });

        if (response.ok) {
            const data = await response.json();
            if (data.status) {
                Notiflix.Report.success(
                    'SmartTrade',
                    data.message,
                    "okay",
                );
                await loadUserData();
            } else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("profile updated failed", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}

async function loadUserData() {
    try {
        const response = await fetch("api/profiles/user-profile");
        if (response.ok) {
            if (response.redirected) {
                window.location.href = response.url;
                return;
            }
            const data = await response.json();

            document.getElementById("username").innerHTML = `Hello, ${data.user.firstName} ${data.user.lastName}`

            let replaceText = String(data.user.sinceAt).replace("-", " ");
            let since = replaceText.split(" ");
            document.getElementById("since").innerHTML = `Smart Trade Member Since ${since[1]} ${since[0]}`;
            document.getElementById("firstName").value = data.user.firstName;
            document.getElementById("lastName").value = data.user.lastName;
            document.getElementById("lineOne").value = data.user.lineOne ? data.user.lineOne : "";
            document.getElementById("lineTwo").value = data.user.lineTwo ? data.user.lineTwo : "";
            document.getElementById("postalCode").value = data.user.postalCode ? data.user.postalCode : "";
            document.getElementById("citySelect").value = data.user.cityId ? data.user.cityId : 0;
            document.getElementById("mobile").value = data.user.mobile;
            document.getElementById("currentPassword").value = data.user.password;
        } else {
            Notiflix.Notify.failure("Profile data loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function getCities() {
    try {
        const response = await fetch("api/data/cities");
        if (response.ok) {
            const data = await response.json();
            const citySelect = document.getElementById("citySelect");
            data.cities.forEach((city) => {
                const option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                citySelect.appendChild(option); // add component as a last child
            })
        } else {
            Notiflix.Notify.failure("City loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function signOut() {
    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch("api/users/logout", {
            method: "GET",
            credentials: "include"
        });
        if (response.ok) {
            Notiflix.Report.success(
                'SmartTrade',
                "Logout successful",
                'Okay', // button title
                () => {
                    window.location = "sign-in.html"
                },
            );
        } else {
            Notiflix.Notify.failure("Something went wrong. Log Out process failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove(1000);
    }
}