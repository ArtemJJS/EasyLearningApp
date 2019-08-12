let textArea = document.getElementsByClassName('replace_br')[0];
let inner = textArea.value;
let newText = inner.split('<br>').join('');
textArea.value = newText;