"use strict";

const baseEndpointUrl = "http://localhost:8080/e-invoice/api";
const locationActive = window.location.pathname;

let response;
let data;

(function () {
  fetchTableInvoice();
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

    var searchField = document.querySelector("input[name=Search]").value;
    var rangeFrom = document.querySelector("input[name=rangeFrom]").value;
    var rangeTo = document.querySelector("input[name=rangeTo]").value;
    var page = 1;

    var obj = {
      search: searchField,
      page: page,
      rangeFrom: rangeFrom,
      rangeTo: rangeTo,
    };

    fetchSearchInvoice(obj);
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

function createFooterPagination(activePage, totalPage) {
  let tfoot = document.querySelector(".table-container > table > tfoot td");

  for (let c = 1; c <= totalPage; c++) {
    var anchor = document.createElement("a");
    anchor.textContent = c;
    anchor.classList = "page-number";

    if (c == activePage) {
      anchor.classList = "page-number active";
    }

    anchor.addEventListener("click", function () {
      var search = document.querySelector("input[name=Search]").value;
      var rangeFrom = document.querySelector("input[name=rangeFrom]").value;
      var rangeTo = document.querySelector("input[name=rangeTo]").value;

      var objPage = {
        search: search,
        page: c,
        rangeFrom: rangeFrom,
        rangeTo: rangeTo,
      };

      fetchPagingInvoice(objPage);
    });

    tfoot.append(anchor);
  }
}

function cetakTableInvoice(listData, activePage, totalPage) {
  let tbody = document.querySelector(".table-container > table > tbody");

  listData.forEach((data) => {
    const baris = document.createElement("tr");

    let status = "";
    if (data.isDraft) {
      status = "Yes";
    } else {
      status = "No";
    }

    baris.append(
      buatKolom(data.invoiceNumber),
      buatKolom(data.spkNumber),
      buatKolom(data.dueDate),
      buatKolom(status),
      buatKolom(data.amountString)
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

    act = document.createElement("td");
    btn = document.createElement("a");
    btn.textContent = "Reminder";
    btn.classList = "yellow-button";
    act.append(btn);
    baris.append(act);
    if (data.isReminder || data.isDraft) {
      btn.style.display = "none";
      btn.style.pointerEvents = "none";
    } else {
      reminderInvoice(btn, data.invoiceNumber);
    }

    tbody.append(baris);
  });

  createFooterPagination(activePage, totalPage);
}

function reminderInvoice(btn, invoiceNumber) {
  btn.addEventListener("click", function () {
    fetchReminderInvoice(btn, invoiceNumber);
  });
}

function deleteInvoice(button, invoiceNumber) {
  button.addEventListener("click", function () {
    fetchDeleteInvoice(invoiceNumber);
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

document.getElementById("sendExcel").addEventListener("click", function () {
  var rangeFrom = document.querySelector("input[name=rangeFrom]").value;
  var rangeTo = document.querySelector("input[name=rangeTo]").value;

  if (rangeFrom == "" || rangeTo == "") {
    printNotif(500, "", "Range From & To can't be null");
  } else {
    fetchDataSendExcel(rangeFrom, rangeTo);
  }
});

async function fetchDataSendExcel(rangeFrom, rangeTo) {
  document.getElementById("loading-box").style.display = "block";

  try {
    const response = await fetch(
      baseEndpointUrl +
        "/invoice/sendExcel?rangeFrom=" +
        rangeFrom +
        "&rangeTo=" +
        rangeTo,
      {
        method: "GET",
      }
    );

    const data = await response.json();

    if (data.data == 200) {
      printNotif(data.code, data.message, data.exception);
    } else {
      printNotif(data.code, data.message, data.exception);
    }
    document.getElementById("loading-box").style.display = "none";
  } catch (error) {
    document.getElementById("loading-box").style.display = "none";
    console.log("Error hit API L", error);
  }
}

async function fetchSearchInvoice(obj) {
  document.getElementById("loading-box").style.display = "block";

  try {
    const response = await fetch(baseEndpointUrl + "/invoice/list", {
      method: "POST",
      body: JSON.stringify(obj),
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();

    clearTable();
    if (data.data.detail.length > 0) {
      cetakTableInvoice(data.data.detail, data.data.page, data.data.totalPage);
    } else {
      errorGetDataTable();
    }
    document.getElementById("loading-box").style.display = "none";
  } catch (error) {
    document.getElementById("loading-box").style.display = "none";
    errorGetDataTable();
    console.log("Error hit API ", error);
  }
}

async function fetchPagingInvoice(objPage) {
  document.getElementById("loading-box").style.display = "block";

  try {
    var response = await fetch(baseEndpointUrl + "/invoice/list", {
      method: "POST",
      body: JSON.stringify(objPage),
      headers: { "Content-Type": "application/json" },
    });

    var data = await response.json();

    clearTable();
    cetakTableInvoice(data.data.detail, data.data.page, data.data.totalPage);
    document.getElementById("loading-box").style.display = "none";
  } catch (error) {
    document.getElementById("loading-box").style.display = "none";
    console.log("Error consume API paging", error);
    printNotif(400, "Error consumes API Paging");
  }
}

async function fetchReminderInvoice(btn, invoiceNumber) {
  try {
    document.getElementById("loading-box").style.display = "block";

    const response = await fetch(
      baseEndpointUrl + "/invoice/reminder?invoiceNumber=" + invoiceNumber,
      {
        method: "GET",
      }
    );
    const data = await response.json();

    if ((data.code = 200)) {
      printNotif(data.code, data.message, data.exception);
      btn.style.pointerEvents = "none";
      btn.style.display = "none";
    } else {
      printNotif(data.code, data.message, data.exception);
    }
    document.getElementById("loading-box").style.display = "none";
  } catch (error) {
    document.getElementById("loading-box").style.display = "none";
    console.log("Error consume API ", error);
  }
}

async function fetchDeleteInvoice(invoiceNumber) {
  document.getElementById("loading-box").style.display = "block";
  try {
    var response = await fetch(
      baseEndpointUrl +
        "/invoice/delete-invoice?invoiceNumber=" +
        invoiceNumber,
      {
        method: "DELETE",
      }
    );

    var data = await response.json();

    clearTable();
    fetchTableInvoice();
    printNotif(data.code, data.message, data.exception);

    document.getElementById("loading-box").style.display = "none";
  } catch (error) {
    document.getElementById("loading-box").style.display = "none";
    console.log("Error delte invoice ", error);
    printNotif(500, "Error delete invoice", "Error delete invoice");
  }
}

async function fetchTableInvoice() {
  var obj = {
    search: "",
    page: 1,
    rangeFrom: "",
    rangeTo: "",
  };
  try {
    document.getElementById("loading-box").style.display = "block";
    const response = await fetch(baseEndpointUrl + "/invoice/list", {
      method: "POST",
      body: JSON.stringify(obj),
      headers: { "Content-Type": "application/json" },
    });
    const data = await response.json();

    document.getElementById("loading-box").style.display = "none";

    cetakTableInvoice(data.data.detail, data.data.page, data.data.totalPage);
  } catch (error) {
    errorGetDataTable();
    document.getElementById("loading-box").style.display = "none";
    console.log("Error fetch data with message : ", error);
  }
}
