/**
 * Created by labo on 2016/8/18.
 */
//导入工具包 require('node_modules里对应模块')
var gulp = require('gulp'), //本地安装gulp所用到的地方
    uglify = require('gulp-uglify'); //代码压缩

/*js压缩*/
gulp.task('jsmin', function () {
    gulp.src(['js/**/*.js'])
        .pipe(uglify({mangle: true}))      //mangle：是否混淆     except: ['require' ,'exports' ,'module' ,'$']
        .pipe(gulp.dest('src/js'))
});
/*注册缺省任务*/
gulp.task('default', ['jsmin']); //定义默认任务 elseTask为其他任务，该示例没有定义elseTask任务

