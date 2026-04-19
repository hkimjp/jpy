function validateAnswer() {
  const answer = document.getElementById("form-answer").value;
  console.log("answer?" + answer);
  if (answer == "") {
    alert("回答がカラです。");
    return false;
  }
}
