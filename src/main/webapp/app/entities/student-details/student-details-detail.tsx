import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-details.reducer';

export const StudentDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentDetailsEntity = useAppSelector(state => state.studentDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsDetailsHeading">
          <Translate contentKey="wednesdayApp.studentDetails.detail.title">StudentDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentDetailsEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="wednesdayApp.studentDetails.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{studentDetailsEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="wednesdayApp.studentDetails.email">Email</Translate>
            </span>
          </dt>
          <dd>{studentDetailsEntity.email}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="wednesdayApp.studentDetails.address">Address</Translate>
            </span>
          </dt>
          <dd>{studentDetailsEntity.address}</dd>
        </dl>
        <Button tag={Link} to="/student-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-details/${studentDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetailsDetail;
