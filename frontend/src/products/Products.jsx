import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchCategories, fetchProducts } from "../store/actions";

const Products = () => {
    const { isLoading, errorMessage } = useSelector((state) => state.errors);
    const { Products, categories, pagination } = useSelector((state) => state.products);
    const dispatch = useDispatch();


    useEffect(() => {
        dispatch(fetchCategories());
    }, [dispatch])
    return (
        <>
        </>
    )
}

