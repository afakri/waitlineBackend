import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './queue.reducer';

export const QueueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const queueEntity = useAppSelector(state => state.queue.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="queueDetailsHeading">Queue</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{queueEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{queueEntity.name}</dd>
          <dt>User</dt>
          <dd>{queueEntity.user ? queueEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/queue" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/queue/${queueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QueueDetail;
