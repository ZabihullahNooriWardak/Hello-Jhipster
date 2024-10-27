import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyEntity from './my-entity';
import Student from './student';
import StudentDetails from './student-details';
import StudClass from './stud-class';
import School from './school';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="my-entity/*" element={<MyEntity />} />
        <Route path="student/*" element={<Student />} />
        <Route path="student-details/*" element={<StudentDetails />} />
        <Route path="stud-class/*" element={<StudClass />} />
        <Route path="school/*" element={<School />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
