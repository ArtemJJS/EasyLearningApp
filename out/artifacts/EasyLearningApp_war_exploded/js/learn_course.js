var videoPlayer = document.getElementById('video_player');
let lessonDivs = document.getElementsByClassName('lesson');

// console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
// console.log(lessonDivs);

function handlerChooseLesson(event) {
    // console.log("11111111111111111111111");
    if (this.matches('.lesson')) {
        // console.log("222222222222222222222");
        var currLessonId = this.id;
        // console.log(lessons_list);
        var lessons = lessons_list.split("CourseLesson");
        console.log('!!!! ' + lessons);
        // var currLessonParts = lessons.filter(function (value) { value.indexOf('id=' + currLessonId)}).join("").split(",");
        var currLessonParts = lessons.filter(function (value) {
            var index = value.indexOf('id=' + currLessonId)
            return index > -1;
        }).join("").split(",");
        // console.log(currLessonParts);
        var newPath = currLessonParts.filter(function (value) {
            var index = value.indexOf('pathToContent');
            return index > -1;
        }).join("").split("=")[1];
        // console.log(newPath);
        videoPlayer.src = newPath;
    }
}

for (let i = 0; i < lessonDivs.length; i++) {
    lessonDivs[i].addEventListener("click", handlerChooseLesson);
}

