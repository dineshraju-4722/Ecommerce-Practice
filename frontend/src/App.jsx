import Products from "./products/Products"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

const App = () => {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/products" element={<Products />} />
        </Routes>
      </Router>
    </>
  )
}

export default App;