import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyEntity from './my-entity';
import MyEntityDetail from './my-entity-detail';
import MyEntityUpdate from './my-entity-update';
import MyEntityDeleteDialog from './my-entity-delete-dialog';

const MyEntityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyEntity />} />
    <Route path="new" element={<MyEntityUpdate />} />
    <Route path=":id">
      <Route index element={<MyEntityDetail />} />
      <Route path="edit" element={<MyEntityUpdate />} />
      <Route path="delete" element={<MyEntityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyEntityRoutes;
