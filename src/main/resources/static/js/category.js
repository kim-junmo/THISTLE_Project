$(document).ready(function() {
    // 1차 카테고리 선택
    $("div#category_menu li.has-children").on("mouseover", function(e) {
        e.preventDefault();

        let cur_category = $(this);

        let cate_code = cur_category.find("a").data("cate_code");

        // 2차 카테고리 주소
        let url = "/admin/category/secondcategory/" + cate_code;

        $.getJSON(url, function(secondcategory_result) {
            let str = '<ul class="dropdown">';

            for (let i = 0; i < secondcategory_result.length; i++) {
                str += '<li>';
                str += `<a class="nav-link active" href="#" th:data-cate_code="${secondcategory_result[i].cate_code}">${secondcategory_result[i].cate_name}</a>`;
                str += '</li>';
            }

            str += "</ul>";
            cur_category.find("ul.dropdown").remove();
            cur_category.append(str);
        });
    });

    // 2차 카테고리 선택
    $("div#category_menu").on("click", "ul.dropdown li a", function(e) {
        e.preventDefault();

        let cate_code = $(this).data("cate_code");
        let cate_name = encodeURIComponent($(this).html()); // 인코딩 작업

        console.log("2차 카테고리 코드", cate_code);
        console.log("2차 카테고리 이름", cate_name);

        // cate_name= 스킨로션 & 특수문자로 인하여 서버에서는 스킨이라는 문자열만 인식됨.
        location.href = `/product/pro_list?cate_code=${cate_code}&cate_name=${cate_name}`;
    });
});