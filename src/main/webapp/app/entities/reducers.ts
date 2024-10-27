import myEntity from 'app/entities/my-entity/my-entity.reducer';
import student from 'app/entities/student/student.reducer';
import studentDetails from 'app/entities/student-details/student-details.reducer';
import studClass from 'app/entities/stud-class/stud-class.reducer';
import school from 'app/entities/school/school.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  myEntity,
  student,
  studentDetails,
  studClass,
  school,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
