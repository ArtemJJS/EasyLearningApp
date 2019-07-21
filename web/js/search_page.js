const PAGE_NUMBER_CLASS = "page_number";
let buttons = document.getElementsByClassName(PAGE_NUMBER_CLASS);
let previousPage = buttons[0];
let nextPage = buttons[1];

// alert("111");
// alert(hasMorePages);

const PAGES_FORM_CLASS = "pages_form";
let forms = document.getElementsByClassName(PAGES_FORM_CLASS);
let formPreviousPage = forms[0];
let formNextPage = forms[1];


if (typeof currPageNumber == "undefined") {
    currPageNumber = 0;
    console.log(1);
}

if (typeof hasMorePages != "undefined") {
    console.log(2);
    nextPage.value = currPageNumber + 1;
}   else {
    console.log(3);
    formNextPage.style.display = "none";
}


if (currPageNumber !== 0){
    console.log(4);
    previousPage.value = currPageNumber - 1;
} else {
    console.log(5);
    formPreviousPage.style.display = "none";
}




