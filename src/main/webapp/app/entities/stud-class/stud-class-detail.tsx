import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stud-class.reducer';

export const StudClassDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studClassEntity = useAppSelector(state => state.studClass.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studClassDetailsHeading">
          <Translate contentKey="wednesdayApp.studClass.detail.title">StudClass</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studClassEntity.id}</dd>
          <dt>
            <span id="className">
              <Translate contentKey="wednesdayApp.studClass.className">Class Name</Translate>
            </span>
          </dt>
          <dd>{studClassEntity.className}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="wednesdayApp.studClass.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{studClassEntity.subject}</dd>
          <dt>
            <Translate contentKey="wednesdayApp.studClass.students">Students</Translate>
          </dt>
          <dd>
            {studClassEntity.students
              ? studClassEntity.students.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {studClassEntity.students && i === studClassEntity.students.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/stud-class" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stud-class/${studClassEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudClassDetail;
