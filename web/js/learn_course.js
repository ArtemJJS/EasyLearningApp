var videoPlayer = document.getElementById('video_player');
// var lessons = document.getElementsByClassName('lesson');
var sectionWithContent = document.getElementById('content_section');

console.log(lessons_list);

function handlerChooseLesson(event) {
    if (event.target.matches('.lesson')){
        var currLessonId = event.target.id;
        // console.log(currLessonId);
        var lessons =  lessons_list.split("CourseLesson");
        // console.log(lessons);
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
       console.log(newPath);
        videoPlayer.src = newPath;
    }
}

sectionWithContent.addEventListener("click", handlerChooseLesson);
