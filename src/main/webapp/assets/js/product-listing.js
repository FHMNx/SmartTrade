document.getElementById("product-anchor").addEventListener("click", async () => {
    Notiflix.Loading.pulse("wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        await loadBrands();
        await loadProductSpecifications();
    } finally {
        Notiflix.Loading.remove();
    }

});

async function loadBrands() {
    try {
        const response = await fetch("api/data/brands");
        if (response.ok) {
            const data = await response.json();

            const brandSelect = document.getElementById("brandSelect");
            brandSelect.innerHTML = `<option value="0">Select></option>`;

            data.brands.forEach((brand) => {
                const option = document.createElement("option");
                option.value = brand.id;
                option.innerHTML = brand.name;
                brandSelect.appendChild(option);
            })
        } else {
            Notiflix.Notify.failure("Brands loading failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function loadModels() {
    Notiflix.Loading.pulse("wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const brandSelect = document.getElementById("brandSelect");

    try {
        const response = await fetch(`api/data/${brandSelect.value}/models`);
        if (response.ok) {
            const data = await response.json();
            const modelSelect = document.getElementById("model");
            modelSelect.innerHTML = `<option value="0">Select</option>`;
            if (data.status) {
                data.models.forEach((model) => {
                    const option = document.createElement("option");
                    option.value = model.id;
                    option.innerHTML = model.name;
                    modelSelect.appendChild(option);
                });
            } else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure("Models loading failed", {
                position: 'center-top'
            });
        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove();
    }
}

async function loadProductSpecifications() {
    try {
        const response = await fetch("api/data/specifications");
        if (response.ok) {
            const data = await response.json();
            const storageSelect = document.getElementById("storage");
            const colorSelect = document.getElementById("color");
            const qualitySelect = document.getElementById("condition");

            renderDropdowns(storageSelect, data.storages, "value");
            renderDropdowns(colorSelect, data.colors, "value");
            renderDropdowns(qualitySelect, data.qualities, "value");
        } else {
            Notiflix.Notify.failure("product specification loading failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

function renderDropdowns(selector, list, suffix) {
    selector.innerHTML = `<option value="0">select</option>`;
    list.forEach((item) => {
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[suffix];
        selector.appendChild(option);
    })
}

async function saveProduct() {
    Notiflix.Loading.pulse("wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    let brandSelect = document.getElementById("brandSelect");
    let modelSelect = document.getElementById("model");
    let title = document.getElementById("title");
    let description = document.getElementById("description");
    let storageSelect = document.getElementById("storage");
    let colorSelect = document.getElementById("color");
    let conditionSelect = document.getElementById("condition");
    let price = document.getElementById("price");
    let qty = document.getElementById("qty");

    let img1 = document.getElementById("img1");
    let img2 = document.getElementById("img2");
    let img3 = document.getElementById("img3");

    const productDataObj = {
        brandId: brandSelect.value,
        modelId: modelSelect.value,
        title: title.value,
        description: description.value,
        storageId: storageSelect.value,
        colorId: colorSelect.value,
        conditionId: conditionSelect.value,
        price: price.value,
        qty: qty.value
    };

    const formData = new FormData();
    formData.append("product", JSON.stringify(productDataObj));
    formData.append("images[]", img1.files[0]);
    formData.append("images[]", img2.files[0]);
    formData.append("images[]", img3.files[0]);

    try {
        const response = await fetch("api/products/save-product", {
            method: "POST",
            body: formData
        });
        if (response.ok) {
            const data = await response.json();
            console.log(data);
        } else {

        }

    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    } finally {
        Notiflix.Loading.remove();
    }
}