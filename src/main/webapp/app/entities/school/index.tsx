import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import School from './school';
import SchoolDetail from './school-detail';
import SchoolUpdate from './school-update';
import SchoolDeleteDialog from './school-delete-dialog';

const SchoolRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<School />} />
    <Route path="new" element={<SchoolUpdate />} />
    <Route path=":id">
      <Route index element={<SchoolDetail />} />
      <Route path="edit" element={<SchoolUpdate />} />
      <Route path="delete" element={<SchoolDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SchoolRoutes;
