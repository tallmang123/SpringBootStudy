import React from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;


/**
import React from 'react';
import logo from './logo.svg';
import './App.css';

class App extends React.Component {
  state = {
    isLoading: true,
  };

  async componentDidMount() {
    const response = await fetch('http://localhost:8080/test');
    const body = await response.json();
    this.setState({isLoading: false });
  }

  render() {
    const {isLoading} = this.state.isLoading;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <div className="App-intro">
            <h2>JUG List</h2>
          </div>
        </header>
      </div>
    );
  }
}

export default App;*/