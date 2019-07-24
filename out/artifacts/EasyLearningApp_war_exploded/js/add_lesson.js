const maxLessons = 10;
const supportingChapterBlockElems = 1;
const maxElemsInsideChapterBlock = maxLessons + supportingChapterBlockElems;

const maxChapters = 10;
const supportingMainFormElems = 6;
const maxElemsInsideMainForm = maxChapters + supportingMainFormElems;

const chapterClass = 'chapter';
const paramClass = 'param';
const lessonParamClass = 'lesson_params';
const lessonsClass = 'lessons';
const addLessonClass = 'add_lesson';
const addLessonBtnText = 'add lesson';
let addLessonBtns = document.getElementsByClassName('add_lesson');
let addChapterBtn = document.getElementsByClassName('add_chapter')[0];


function addMoreLesson(event) {
    let parentElement = event.target.parentElement;
    let childs = parentElement.children;
    let childCount = childs.length;
    if (childCount >= maxElemsInsideChapterBlock) {
        // addLessonBtn.style.display = 'none';
        findAncestor(event.target, chapterClass).getElementsByClassName(addLessonClass)[0].style.display = 'none';
        alert("You have reached lessons amount limit to this chapter!");
        return;
    }

    //здесь передавался парент сразу первый
    let parentChapter = findAncestor(event.target, chapterClass);

    let addedLesson = createLessonInput(childCount, parentChapter.id);
    parentElement.insertBefore(addedLesson, childs[childCount - 1]);
}

function addMoreChapter(event) {
    let parentElement = event.target.parentElement;
    let childs = parentElement.children;
    let childCount = childs.length;
    if (childCount >= maxElemsInsideMainForm) {
        addChapterBtn.style.display = 'none';
        alert("You have reached chapter amount limit to this course!");
        return;
    }
    // +1 because we already have one chapter block
    let addedChapter = createChapterInput(childCount - supportingMainFormElems + 1);
    parentElement.insertBefore(addedChapter, childs[childCount - 3]);
}


function createLessonInput(index, parentId) {
    let div = document.createElement("div");
    div.classList.add(paramClass);
    div.classList.add("lesson_" + index);

    let label = document.createElement("div");
    label.setAttribute("for", "lesson_field_" + index);
    label.innerText = "Lesson " + index + ":";

    let lessonTitle = document.createElement("input");
    lessonTitle.setAttribute("type", "by.anelkin.easylearning.text");
    lessonTitle.setAttribute("id", "lesson_field_" + index);
    lessonTitle.setAttribute("name", "lesson_title_" + parentId);
    lessonTitle.setAttribute("placeholder", "Lesson title");
    lessonTitle.setAttribute("pattern", "[A-z0-9_ -]{3,150}");

    let lessonContent = document.createElement("input");
    lessonContent.setAttribute("type", "by.anelkin.easylearning.text");
    lessonContent.setAttribute("name", "lesson_content_" + parentId);
    lessonContent.setAttribute("placeholder", "Lesson content");

    let lessonDuration = document.createElement("input");
    lessonDuration.setAttribute("type", "by.anelkin.easylearning.text");
    lessonDuration.setAttribute("name", "lesson_duration_" + parentId);
    lessonDuration.setAttribute("placeholder", "Lesson duration");
    lessonDuration.setAttribute("pattern", "[0-9]{1,10}");

    let paramWrapper = document.createElement("div");
    paramWrapper.classList.add(lessonParamClass);
    paramWrapper.appendChild(lessonTitle);
    paramWrapper.appendChild(lessonContent);
    paramWrapper.appendChild(lessonDuration);

    div.appendChild(label);
    div.appendChild(paramWrapper);
    return div;
}


function createChapterInput(index) {
    let label = document.createElement("label");
    label.setAttribute("for", "chapter_field_" + index);
    label.innerText = "Chapter " + index + ":";

    let chapterTitle = document.createElement("input");
    chapterTitle.setAttribute("type", "by.anelkin.easylearning.text");
    chapterTitle.setAttribute("id", "chapter_field_" + index);
    chapterTitle.setAttribute("name", "chapter_name");
    chapterTitle.setAttribute("placeholder", "Chapter title");
    chapterTitle.setAttribute("pattern", "[A-z0-9_ -]{1,200}");
    chapterTitle.setAttribute("required", "true");

    let chapterInput = document.createElement("div");
    chapterInput.classList.add(chapterClass);
    chapterInput.classList.add(paramClass);
    chapterInput.appendChild(label);
    chapterInput.appendChild(chapterTitle);

    //<div class="add_lesson">add lesson</div>
    let oneMoreLessonBtn = document.createElement("div");
    oneMoreLessonBtn.classList.add(addLessonClass);
    oneMoreLessonBtn.innerText = addLessonBtnText;
    oneMoreLessonBtn.addEventListener("click", addMoreLesson);

    let lessonsWrapper = document.createElement("div");
    lessonsWrapper.classList.add(lessonsClass);
    lessonsWrapper.appendChild(createLessonInput(1, index));
    lessonsWrapper.appendChild(oneMoreLessonBtn);

    let div = document.createElement("div");
    div.setAttribute("id", "" + index);
    div.classList.add(chapterClass);
    div.appendChild(chapterInput);
    div.appendChild(lessonsWrapper);

    return div;
}


function findAncestor(elem, cls) {
    while ((elem = elem.parentElement) && !elem.classList.contains(cls)) {
    }
    return elem;
}


for (let i = 0; i < addLessonBtns.length; i++) {
    addLessonBtns[i].addEventListener("click", addMoreLesson);
}

addChapterBtn.addEventListener('click', addMoreChapter);