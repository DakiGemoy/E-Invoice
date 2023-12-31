"use strict";

const baseEndpointUrl = "http://localhost:8080/e-invoice/api";
const locationActive = window.location.pathname;

let response;
let data;

(function () {
  initTableInvoice();
})();

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

function createDropdown(dataDropdown, target) {
  dataDropdown.forEach((dd) => {
    var opt = document.createElement("option");
    opt.value = dd.value;
    opt.textContent = dd.name;

    target.append(opt);
  });
}

document
  .getElementById("search-invoice")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    var searchField = document.querySelector("input[name=search]").value;
    var page = 1;

    fetch(
      baseEndpointUrl + "/invoice/list?search=" + searchField + "&page=" + page,
      {
        method: "GET",
      }
    )
      .then((response) => response.json())
      .then((data) => {
        clearTable();
        if (data.data.detail.length > 0) {
          cetakTableInvoice(
            data.data.detail,
            data.data.page,
            data.data.totalPage
          );
        } else {
          errorGetDataTable();
        }
      })
      .catch((error) => {
        errorGetDataTable();
      });
  });

function errorGetDataTable() {
  var tbody = document.querySelector(".table-container > table > tbody");

  var tr = document.createElement("tr");
  var td = document.createElement("td");
  td.colSpan = 7;

  td.textContent = "No data to show";
  tr.append(td);

  tbody.append(tr);
}

function initTableInvoice() {
  fetch(baseEndpointUrl + "/invoice/list", {
    method: "GET",
  })
    .then((response) => response.json())

    .then((data) => {
      cetakTableInvoice(data.data.detail, data.data.page, data.data.totalPage);
    })
    .catch((error) => {
      errorGetDataTable();
    });
}

function createFooterPagination(activePage, totalPage) {
  let tfoot = document.querySelector(".table-container > table > tfoot td");
  var searchField = document.querySelector("input[name=search]").value;

  for (let c = 1; c <= totalPage; c++) {
    var anchor = document.createElement("a");
    anchor.textContent = c;
    anchor.classList = "page-number";

    if (c == activePage) {
      anchor.classList = "page-number active";
    }

    anchor.addEventListener("click", function () {
      fetch(
        baseEndpointUrl + "/invoice/list?search=" + searchField + "&page=" + c,
        {
          method: "GET",
        }
      )
        .then((response) => response.json())
        .then((data) => {
          clearTable();
          cetakTableInvoice(
            data.data.detail,
            data.data.page,
            data.data.totalPage
          );
        });
    });

    tfoot.append(anchor);
  }
}

function cetakTableInvoice(listData, activePage, totalPage) {
  let tbody = document.querySelector(".table-container > table > tbody");

  listData.forEach((data) => {
    const baris = document.createElement("tr");

    baris.append(
      buatKolom(data.invoiceNumber),
      buatKolom(data.spkNumber),
      buatKolom(data.dueDate),
      buatKolom(data.isDraft),
      buatKolom(data.amount)
    );

    let act = document.createElement("td");
    let btn = document.createElement("a");
    btn.textContent = "Detail";
    btn.classList = "blue-button";
    btn.href = "http://localhost:8080/e-invoice/invoice/detail?invoiceNumber=" + data.invoiceNumber;
    act.append(btn);
    baris.append(act);

    act = document.createElement("td");
    btn = document.createElement("a");
    btn.textContent = "Delete";
    btn.classList = "red-button";
    act.append(btn);
    baris.append(act);
    if (!data.isDraft) {
      btn.addEventListener("click", function () {
        printNotif(400, "Data can't be delete", "Data can't be delete");
      });
    } else {
      deleteInvoice(btn, data.invoiceNumber);
    }

    tbody.append(baris);
  });

  createFooterPagination(activePage, totalPage);
}

function deleteInvoice(button, invoiceNumber) {
  button.addEventListener("click", function () {
    fetch(
      baseEndpointUrl +
        "/invoice/delete-invoice?invoiceNumber=" +
        invoiceNumber,
      {
        method: "DELETE",
      }
    )
      .then((response) => response.json())
      .then((data) => {
        clearTable();
        initTableInvoice();
        printNotif(data.code, data.message, data.exception);
      });
  });
}

function buatKolom(data) {
  let kolom = document.createElement("td");
  kolom.textContent = data;

  return kolom;
}

function clearTable() {
  let tableBody = document.querySelector(".table-container > table > tbody");
  let tableFoot = document.querySelector(".table-container > table > tfoot td");

  while (tableBody.firstChild) {
    tableBody.removeChild(tableBody.lastChild);
  }

  while (tableFoot.firstChild) {
    tableFoot.removeChild(tableFoot.lastChild);
  }
}
