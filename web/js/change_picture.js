const CLASS_TARGET_IS_ACC = 'target_is_acc';
const CLASS_TARGET_IS_COURSE = 'target_is_course';

let isTargetAcc = (typeof courseId == 'undefined');

let elements;
if (isTargetAcc) {
    elements = document.getElementsByClassName(CLASS_TARGET_IS_COURSE);
} else {
    elements = document.getElementsByClassName(CLASS_TARGET_IS_ACC);
}

for (let i = elements.length -1 ; i >= 0; i--) {
    removeSelf(elements[i]);
}

function removeSelf(targ){
    console.log("remove function");
    let parent = targ.parentElement;
    parent.removeChild(targ);
}

