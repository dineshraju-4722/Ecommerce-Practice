import { configureStore } from "@reduxjs/toolkit";
import { ProductReducer } from "./ProductReducer";
import { ErrorReducer} from "./ErrorReducer"



export const store = configureStore({
    reducer:{
        products : ProductReducer  ,
        errors : ErrorReducer ,
    }
})