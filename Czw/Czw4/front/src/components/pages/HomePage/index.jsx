import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Dimmer, Loader, Table} from "semantic-ui-react";
import booksApi from "../../../api/books";
import _ from 'lodash';


class HomePage extends Component {
    state = {
        errors: {},
        books: [],
        loading: true
    };

    componentDidMount = async () => {
        const books = await booksApi.getAllBooks();
        console.log(books);
        this.setState({books, loading: false})
    };

    render() {
        const {books} = this.state;
        if (_.isEmpty(books)) return <Dimmer active><Loader size='massive'>Loading</Loader></Dimmer>;
        else return (
            <Table compact='very' striped celled textAlign='center'>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>ID</Table.HeaderCell>
                        <Table.HeaderCell>Tytul</Table.HeaderCell>
                        <Table.HeaderCell>Autor</Table.HeaderCell>
                        <Table.HeaderCell>Rok</Table.HeaderCell>
                        <Table.HeaderCell>Cena</Table.HeaderCell>
                        <Table.HeaderCell>Wydawca</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>

                <Table.Body>
                    {books.map((book, index) => {
                        const {id, autor, wydawca, tytul, cena, rok} = book;
                        return (<Table.Row key={id}>
                            <Table.Cell>{id}</Table.Cell>
                            <Table.Cell>{tytul}</Table.Cell>
                            <Table.Cell>{autor.name}</Table.Cell>
                            <Table.Cell>{rok}</Table.Cell>
                            <Table.Cell>{cena}</Table.Cell>
                            <Table.Cell>{wydawca.name}</Table.Cell>
                        </Table.Row>)
                    })}
                </Table.Body>
            </Table>
        );
    }
}


HomePage.propTypes = {};

export default HomePage;