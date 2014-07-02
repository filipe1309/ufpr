require 'test_helper'

class BooksStoresControllerTest < ActionController::TestCase
  setup do
    @books_store = books_stores(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:books_stores)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create books_store" do
    assert_difference('BooksStore.count') do
      post :create, books_store: { book_id: @books_store.book_id, store_id: @books_store.store_id }
    end

    assert_redirected_to books_store_path(assigns(:books_store))
  end

  test "should show books_store" do
    get :show, id: @books_store
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @books_store
    assert_response :success
  end

  test "should update books_store" do
    patch :update, id: @books_store, books_store: { book_id: @books_store.book_id, store_id: @books_store.store_id }
    assert_redirected_to books_store_path(assigns(:books_store))
  end

  test "should destroy books_store" do
    assert_difference('BooksStore.count', -1) do
      delete :destroy, id: @books_store
    end

    assert_redirected_to books_stores_path
  end
end
