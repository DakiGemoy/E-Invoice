"use strict";

const baseEndpointUrl = "http://localhost:8080/e-invoice/api";
const locationActive = window.location.pathname;

let response;
let data;

(function () {
  if (locationActive == "/e-invoice/client/list") {
    getListClient();
  }
})();

function openForm() {
  document.getElementById("modal-popup").style.display = "flex";
}

function closeForm() {
  var form = document.getElementById("client-upsert");
  form.reset();
  document.getElementById("modal-popup").style.display = "none";
}

document
  .getElementById("client-upsert")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    var code = document.querySelector('input[name="clientCode"]').value;
    var name = document.querySelector('input[name="clientName"]').value;
    var address = document.querySelector(
      'textarea[name="clientAddress"]'
    ).value;
    var email = document.querySelector('input[name="clientEmail"]').value;
    var phoneNumber = document.querySelector('input[name="clientPhone"]').value;

    var data = {
      code: code,
      name: name,
      address: address,
      email: email,
      phone: phoneNumber,
    };

    fetch("http://localhost:8080/e-invoice/api/client/upsert", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((data) => {
        printNotif(data.code, data.message);
      });
    closeForm();
  });

function printNotif(status, msg) {
  var notifElement = document.getElementById("notif");

  notifElement.style.display = "flex";
  notifElement.style.opacity = 1;

  if (status == 200) {
    notifElement.classList.add("success");
    document.querySelector("#notif p").textContent = msg;
  } else {
    notifElement.classList.add("error");
    document.querySelector("#notif p").textContent = msg;
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

function getListClient() {
  const ajaxLocal = new XMLHttpRequest();
  ajaxLocal.open("GET", baseEndpointUrl + "/client/getList");
  ajaxLocal.send();

  ajaxLocal.onload = function () {
    response = JSON.parse(ajaxLocal.responseText);
    if (response.code == 200) {
      cetakTableClientList(response.data);
    } else {
      console.log("Error consume API with message : " + response.message);
    }
  };
}

function cetakTableClientList(listData) {
  let tbody = document.querySelector(".table-container > table > tbody");

  listData.forEach((data) => {
    const baris = document.createElement("tr");

    baris.append(
      buatKolom(data.clientCode),
      buatKolom(data.clientName),
      buatKolom(data.clientAddress)
    );

    let act = document.createElement("td");
    let btn = document.createElement("a");
    btn.textContent = "edit";
    btn.classList = "blue-button";
    act.append(btn);
    baris.append(act);

    act = document.createElement("td");
    btn = document.createElement("a");
    btn.textContent = "delete";
    btn.classList = "red-button";
    act.append(btn);
    baris.append(act);

    // baris.append(kol1, kol2, kol3, act1, act2);
    tbody.append(baris);
  });

  function buatKolom(data) {
    let kolom = document.createElement("td");
    kolom.textContent = data;

    return kolom;
  }
}

function clearTable() {
  let tableBody = document.querySelector(".table-container > table > tbody");

  while (tableBody.firstChild) {
    tableBody.removeChild(tableBody.lastChild);
  }
}

document
  .getElementById("search-form")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    var searchInput = document.querySelector('input[name="clientName"]').value;

    const ajaxLocal = new XMLHttpRequest();

    ajaxLocal.open(
      "GET",
      baseEndpointUrl + "/client/getList?search=" + searchInput
    );
    ajaxLocal.send();

    ajaxLocal.onload = function () {
      response = JSON.parse(ajaxLocal.responseText);

      if (response.code == 200) {
        clearTable();
        cetakTableClientList(response.data);
      } else {
        console.log("Error consume API with message : " + response.message);
      }
    };
  });
