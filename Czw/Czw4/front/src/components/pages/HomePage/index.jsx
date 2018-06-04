import React, {Component} from 'react';
import {Form, Grid, Table} from "semantic-ui-react";
import booksApi from "../../../api/books";

class HomePage extends Component {
    state = {
        errors: {},
        books: [],
        formData: {
            tytul: '',
            autor: '',
            rok: ''
        },
        loading: true
    };

    componentDidMount = async () => {
        try {
            const books = await booksApi.getAllBooks();
            console.log(books);
            this.setState({books, loading: false})
        } catch (err) {
            this.setState({loading: false});
        }
    };

    onFormChange = (e, data) => this.setState({formData: {...this.state.formData, [data.name]: data.value}});

    onFormSubmit = async () => {
        try {
            const {autor, tytul, rok} = this.state.formData;
            this.setState({loading: true});
            const books = await booksApi.getFilteredBooks({
                autor: autor ? autor : null,
                tytul: tytul ? tytul : null,
                rok: rok ? rok : null,
            });
            console.log(books);
            this.setState({books, loading: false})
        } catch (err) {
            this.setState({loading: false});
        }
    };

    render() {
        const {books, loading, formData} = this.state;
        return (
            <Grid>
                <Grid.Row>
                    <Form loading={loading} onSubmit={this.onFormSubmit}>
                        <Form.Group>
                            <Form.Input label={'Tytul'} id={'tytul'} name={'tytul'} onChange={this.onFormChange}/>
                            <Form.Input label={'Autor'} id={'autor'} name={'autor'} onChange={this.onFormChange}/>
                            <Form.Input label={'Rok'} id={'rok'} name={'rok'} onChange={this.onFormChange}/>
                        </Form.Group>
                        <Form.Button fluid primary content={'Search'}/>
                    </Form>
                </Grid.Row>
                <Grid.Row>
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
                </Grid.Row>
            </Grid>
        );
    }
}


HomePage.propTypes = {};

export default HomePage;