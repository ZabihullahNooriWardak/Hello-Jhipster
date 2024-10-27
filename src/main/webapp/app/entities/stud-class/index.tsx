import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudClass from './stud-class';
import StudClassDetail from './stud-class-detail';
import StudClassUpdate from './stud-class-update';
import StudClassDeleteDialog from './stud-class-delete-dialog';

const StudClassRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudClass />} />
    <Route path="new" element={<StudClassUpdate />} />
    <Route path=":id">
      <Route index element={<StudClassDetail />} />
      <Route path="edit" element={<StudClassUpdate />} />
      <Route path="delete" element={<StudClassDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudClassRoutes;
