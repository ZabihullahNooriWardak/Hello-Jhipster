import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentDetails from './student-details';
import StudentDetailsDetail from './student-details-detail';
import StudentDetailsUpdate from './student-details-update';
import StudentDetailsDeleteDialog from './student-details-delete-dialog';

const StudentDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentDetails />} />
    <Route path="new" element={<StudentDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<StudentDetailsDetail />} />
      <Route path="edit" element={<StudentDetailsUpdate />} />
      <Route path="delete" element={<StudentDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentDetailsRoutes;
