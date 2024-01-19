import * as React from 'react';
import Box from '@mui/joy/Box';
import Column from './Column';
import { DragDropContext } from 'react-beautiful-dnd';
const initialColumns = {
  Waiting: {
    name: 'Waiting',
    items: [
      {
        id: '0e2f0db1-5457-46b0-949e-8032d2f9997a',
        name: 'Alex Jonnold',
      },
      {
        id: '0e2f0db1-5457-46b0-949e-8032d2f99971',
        name: 'Pete Sand',
      },
      {
        id: '0e2f0db1-5457-46b0-949e-8032d2f99972',
        name: 'Kate Gates',
      },
      {
        id: '0e2f0db1-5457-46b0-949e-8032d2f99973',
        name: 'John Snow',
      },
      {
        id: '0e2f0db1-5457-46b0-949e-8032d2f99974',
        name: 'Michael Scott',
      },
    ],
  },
  Summoned: {
    name: 'Summoned',
    items: [],
  },
  Done: {
    name: 'Done',
    items: [],
  },
};

export default function Board() {
  const [columns, setColumns] = React.useState(initialColumns);

  const handleDragAndDrop = result => {
    const { source, destination } = result;

    // If dropped outside the list
    if (!destination) {
      return;
    }

    // If the location of the draggable item did not change
    if (source.droppableId === destination.droppableId && source.index === destination.index) {
      return;
    }

    // Find the column from which the item is dragged
    const sourceColumn = columns[source.droppableId];
    const destColumn = columns[destination.droppableId];

    // Copy the item references
    const sourceItems = [...sourceColumn.items];
    const destItems = destination.droppableId === source.droppableId ? sourceItems : [...destColumn.items];

    // Remove the item from its original position
    const [removed] = sourceItems.splice(source.index, 1);

    // Insert the item in the new position
    destItems.splice(destination.index, 0, removed);

    // Update the state
    setColumns({
      ...columns,
      [source.droppableId]: {
        ...sourceColumn,
        items: sourceItems,
      },
      [destination.droppableId]: {
        ...destColumn,
        items: destItems,
      },
    });
  };

  return (
    <React.Fragment>
      <DragDropContext onDragEnd={handleDragAndDrop}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            columnGap: 2,
            height: '100%',
          }}
        >
          <Column data={columns.Waiting} title="Waiting" color="warning" />
          <Column data={columns.Summoned} title="Summoned" color="neutral" />
          <Column data={columns.Done} title="Done" color="success" />
        </Box>
      </DragDropContext>
    </React.Fragment>
  );
}
