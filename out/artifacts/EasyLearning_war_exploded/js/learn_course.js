let videoPlayer = document.getElementById('video_player');
let lessonDivs = document.getElementsByClassName('lesson');

// set first lesson's video to player when page loading
 setFirstLessonPathToPlayer = function () {
     let firstLesson = document.getElementsByClassName("lesson")[0];
     let currLessonId = firstLesson.id;
     let lessons = lessons_list.split("CourseLesson");
     let currLessonParts = lessons.filter(function (value) {
         let index = value.indexOf('id=' + currLessonId);
         return index > -1;
     }).join("").split(",");
     let newPath = currLessonParts.filter(function (value) {
         let index = value.indexOf('pathToContent');
         return index > -1;
     }).join("").split("=")[1];
     videoPlayer.src = newPath;
 }();


function handlerChooseLesson(event) {
    if (this.matches('.lesson')) {
        let currLessonId = this.id;
        let lessons = lessons_list.split("CourseLesson");
        let currLessonParts = lessons.filter(function (value) {
            let index = value.indexOf('id=' + currLessonId);
            return index > -1;
        }).join("").split(",");
        let newPath = currLessonParts.filter(function (value) {
            let index = value.indexOf('pathToContent');
            return index > -1;
        }).join("").split("=")[1];
        videoPlayer.src = newPath;
    }
}

for (let i = 0; i < lessonDivs.length; i++) {
    lessonDivs[i].addEventListener("click", handlerChooseLesson);
}

