
function showViewAddCourse(){
    if (document.getElementById("view-add-course").hidden){
        document.getElementById("view-add-course").hidden = false;
        document.getElementById("btn-new-course").className = "btn btn-danger";
        document.getElementById("btn-new-course").textContent = "Cancelar";
    }
    else{
        document.getElementById("view-add-course").hidden = true;
        document.getElementById("btn-new-course").className = "btn btn-primary";
        document.getElementById("btn-new-course").textContent = "Agregar Nuevo Curso";
    }
}