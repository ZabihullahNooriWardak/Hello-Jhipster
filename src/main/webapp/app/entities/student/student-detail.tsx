import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">
          <Translate contentKey="wednesdayApp.student.detail.title">Student</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="wednesdayApp.student.name">Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.name}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="wednesdayApp.student.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <Translate contentKey="wednesdayApp.student.studentDetails">Student Details</Translate>
          </dt>
          <dd>{studentEntity.studentDetails ? studentEntity.studentDetails.id : ''}</dd>
          <dt>
            <Translate contentKey="wednesdayApp.student.school">School</Translate>
          </dt>
          <dd>{studentEntity.school ? studentEntity.school.id : ''}</dd>
          <dt>
            <Translate contentKey="wednesdayApp.student.studClasses">Stud Classes</Translate>
          </dt>
          <dd>
            {studentEntity.studClasses
              ? studentEntity.studClasses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {studentEntity.studClasses && i === studentEntity.studClasses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
