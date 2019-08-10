const PAGE_NUMBER_CLASS = "page_number";
let buttons = document.getElementsByClassName(PAGE_NUMBER_CLASS);
let previousPage = buttons[0];
let nextPage = buttons[1];

const PAGES_FORM_CLASS = "pages_form";
let forms = document.getElementsByClassName(PAGES_FORM_CLASS);
let formPreviousPage = forms[0];
let formNextPage = forms[1];


if (typeof currPageNumber == "undefined") {
    currPageNumber = 0;
}

if (typeof hasMorePages != "undefined") {
    nextPage.value = currPageNumber + 1;
} else {
    formNextPage.style.display = "none";
}

if (currPageNumber !== 0) {
    previousPage.value = currPageNumber - 1;
} else {
    formPreviousPage.style.display = "none";
}




