import Products from "./components/products/Products"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/shared/Navbar";

const App = () => {
  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/products" element={<Products />} />
        </Routes>
      </Router>
    </>
  )
}

export default App;