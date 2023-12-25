"use strict";

const baseEndpointUrl = "http://localhost:8080/e-invoice/api";
const locationActive = window.location.pathname;

let response;
let data;

(function () {
  let baseParam = new URLSearchParams(location.search);
  initData(baseParam.get("invoiceNumber"));
})();

function initData(invoiceNumber) {
  let endpoint = baseEndpointUrl;

  if (invoiceNumber != null) {
    endpoint = endpoint + "/invoice/get-invoice?invoiceNumber=" + invoiceNumber;
  } else {
    endpoint = endpoint + "/invoice/get-invoice";
  }

  fetch(endpoint, {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.data != null) {
        setDataToFormInvoice(data.data);
      } else {
        loadDropdownClient();
      }
    });
}

function setDataToFormInvoice(dataForm) {
  document.querySelector("input[name=invoice]").value = dataForm.invoiceNumber;
  loadDropdownClientExists(dataForm.clientCode);
  document.querySelector("select[name=clientCode]").disabled = true;
  document.querySelector("input[name=spkNum]").value = dataForm.spkNumber;
  document.querySelector("textarea[name=notes]").value = dataForm.notes;
  document.querySelector("input[name=dueDate]").value = dataForm.dueDate;

  if (dataForm.status) {
    document.querySelector("input[name=status]").checked = true;
  } else {
    document.querySelector("input[name=status]").checked = false;
    document.querySelector("input[name=status]").disabled = true;

    document.querySelector("input[name=spkNum]").disabled = true;
    document.querySelector("textArea[name=notes]").disabled = true;
    document.querySelector("input[name=dueDate]").disabled = true;
  }

  cetakTableDescription(dataForm.desc, dataForm.invoiceNumber, dataForm.status);
}

function openForm() {
  var invNumber = document.querySelector("input[name=invoice]").value;
  if (invNumber != "") {
    fetch(baseEndpointUrl + "/invoice/check?invoiceNumber=" + invNumber, {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => {
        if (data) {
          document.getElementById("modal-popup").style.display = "flex";
          loadDropdownCar();
        }
      });
  }
}

function printNotif(status, msg, exc) {
  var notifElement = document.getElementById("notif");

  notifElement.style.display = "flex";
  notifElement.style.opacity = 1;

  if (status == 200) {
    notifElement.classList.add("success");
    document.querySelector("#notif p").textContent = msg;
  } else {
    notifElement.classList.add("error");
    console.log(msg + " : " + exc);
    document.querySelector("#notif p").textContent = exc;
  }

  setTimeout(function () {
    notifElement.style.opacity = 0;
    setTimeout(function () {
      notifElement.classList.remove("success");
      notifElement.classList.remove("error");
      notifElement.classList.remove("warning");
      notifElement.style.display = "none";
    }, 1000);
  }, 1000);
}

function closeForm() {
  var form = document.getElementById("car-insert");
  form.reset();
  clearOption(document.querySelector("select[name=carSelect]"));
  document.getElementById("modal-popup").style.display = "none";
}

function clearOption(obj) {
  while (!obj.lastChild.disabled) {
    obj.removeChild(obj.lastChild);
  }
}

function loadDropdownClientExists(clientSelected) {
  fetch(baseEndpointUrl + "/dropdown/client", {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      createDropdown(
        data.data,
        document.getElementById("clientCode"),
        clientSelected
      );
    });
}

function loadDropdownClient() {
  fetch(baseEndpointUrl + "/dropdown/client", {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      createDropdown(data.data, document.getElementById("clientCode"));
    });
}

function loadDropdownCar() {
  fetch(baseEndpointUrl + "/dropdown/vehicle", {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      createDropdown(
        data.data,
        document.querySelector("select[name=carSelect]")
      );
    });
}

function loadDropdownCarExist(carSelected) {
  fetch(baseEndpointUrl + "/dropdown/vehicle", {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      createDropdown(
        data.data,
        document.querySelector("select[name=carSelect]"),
        carSelected
      );
    });
}

function createDropdown(dataDropdown, target) {
  dataDropdown.forEach((dd) => {
    var opt = document.createElement("option");
    opt.value = dd.value;
    opt.textContent = dd.name;

    target.append(opt);
  });
}

function createDropdown(dataDropdown, target, dataSelected) {
  dataDropdown.forEach((dd) => {
    var opt = document.createElement("option");
    opt.value = dd.value;
    opt.textContent = dd.name;
    if (dataSelected == dd.value) {
      opt.selected = true;
    }

    target.append(opt);
  });
}

document
  .getElementById("invoice-input")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    var invoice = document.querySelector("input[name=invoice]").value;
    var client = document.querySelector("select[name=clientCode]").value;
    var spk = document.querySelector("input[name=spkNum]").value;
    var notes = document.querySelector("textarea[name=notes]").value;
    var duedate = document.querySelector("input[name=dueDate]").value;
    var status = document.querySelector("input[name=status]").checked;
    var creator = document.getElementById("user-loged").textContent;

    var obj = {
      invoiceNumber: invoice,
      spkNumber: spk,
      notes: notes,
      clientCode: client,
      dueDate: duedate,
      status: status,
      createdBy: creator,
    };

    fetch(baseEndpointUrl + "/invoice/save-invoice", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(obj),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.code == 200) {
          printNotif(data.code, data.message, data.exception);
          clearTable();
          initData(invoice);
        } else {
          printNotif(data.code, data.message, data.exception);
        }
      });
    closeForm();
  });

document
  .getElementById("car-insert")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    var descId = document.querySelector("input[name=descId]").value;
    var carSelect = document.querySelector("select[name=carSelect]").value;
    var invoice = document.querySelector("input[name=invoice]").value;
    var rentFrom = document.querySelector("input[name=rent-from]").value;
    var rentTo = document.querySelector("input[name=rent-to]").value;
    var price = document.querySelector("input[name=price]").value;

    const carObj = {
      descId: descId,
      invoiceNumber: invoice,
      vehicleId: carSelect,
      rentFrom: rentFrom,
      rentTo: rentTo,
      price: price,
    };

    fetch(baseEndpointUrl + "/invoice/add-car", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(carObj),
    })
      .then((response) => response.json())
      .then((data) => {
        if ((data.code = 200)) {
          printNotif(data.code, data.message, data.exception);
          clearTable();
          initData(invoice);
          closeForm();
        } else {
          printNotif(data.code, data.message, data.exception);
        }
      })
      .catch((error) => {
        console.log("Error while save data with message : " + data.message);
      });
  });

function cetakTableDescription(listData, invoice, status) {
  let tbody = document.querySelector(".table-container > table > tbody");

  listData.forEach((data) => {
    const baris = document.createElement("tr");

    baris.append(
      buatKolom(data.vehicle),
      buatKolom(data.rentFrom),
      buatKolom(data.rentTo),
      buatKolom(data.price)
    );

    let act = document.createElement("td");
    let btn = document.createElement("a");
    btn.textContent = "Edit";
    btn.classList = "yellow-button";
    act.append(btn);
    baris.append(act);
    if (status) {
      editCar(btn, data.id);
    }

    act = document.createElement("td");
    btn = document.createElement("a");
    btn.textContent = "Delete";
    btn.classList = "red-button";
    act.append(btn);
    baris.append(act);
    if (status) {
      deleteCar(btn, data.id, invoice);
    }

    tbody.append(baris);
  });
}

function editCar(object, uniqueId) {
  object.addEventListener("click", function () {
    fetch(baseEndpointUrl + "/invoice/get-desc?descId=" + uniqueId, {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("modal-popup").style.display = "flex";
        loadDropdownCarExist(data.data.vehicleId);
        setDataToFormCar(data.data);
      });
  });
}

function deleteCar(object, uniqueId, invoiceNumber) {
  object.addEventListener("click", function () {
    fetch(baseEndpointUrl + "/invoice/delete-desc?descId=" + uniqueId, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.code) {
          printNotif(data.code, data.message, data.exception);
          clearTable();
          initData(invoiceNumber);
        } else {
          printNotif(data.code, data.message, data.exception);
        }
      });
  });
}

function setDataToFormCar(desc) {
  document.querySelector("input[name=descId]").value = desc.id;
  document.querySelector("input[name=rent-from]").value = desc.rentFrom;
  document.querySelector("input[name=rent-to]").value = desc.rentTo;
  document.querySelector("input[name=price]").value = desc.price;
}

function buatKolom(data) {
  let kolom = document.createElement("td");
  kolom.textContent = data;

  return kolom;
}

function clearTable() {
  let tableBody = document.querySelector(".table-container > table > tbody");

  while (tableBody.firstChild) {
    tableBody.removeChild(tableBody.lastChild);
  }
}

//generate invoice number
document.getElementById("clientCode").addEventListener("change", function () {
  var client = document.getElementById("clientCode").value;

  fetch(baseEndpointUrl + "/invoice/generate?clientCode=" + client, {
    method: "GET",
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.code == 200) {
        document.querySelector("input[name='invoice']").value = data.data;
      } else {
        console.log("Error consume API with message : " + data.message);
      }
    })
    .catch((error) => {
      console.log("Error consume API with message : " + data.message);
    });
});
