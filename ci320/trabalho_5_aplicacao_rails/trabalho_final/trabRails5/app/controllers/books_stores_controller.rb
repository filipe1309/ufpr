class BooksStoresController < ApplicationController
  before_action :set_books_store, only: [:show, :edit, :update, :destroy]

  # GET /books_stores
  # GET /books_stores.json
  def index
    @books_stores = BooksStore.all
  end

  # GET /books_stores/1
  # GET /books_stores/1.json
  def show
  end

  # GET /books_stores/new
  def new
    @books_store = BooksStore.new
  end

  # GET /books_stores/1/edit
  def edit
  end

  # POST /books_stores
  # POST /books_stores.json
  def create
    @books_store = BooksStore.new(books_store_params)

    respond_to do |format|
      if @books_store.save
        format.html { redirect_to @books_store, notice: 'Books store was successfully created.' }
        format.json { render action: 'show', status: :created, location: @books_store }
      else
        format.html { render action: 'new' }
        format.json { render json: @books_store.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /books_stores/1
  # PATCH/PUT /books_stores/1.json
  def update
    respond_to do |format|
      if @books_store.update(books_store_params)
        format.html { redirect_to @books_store, notice: 'Books store was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: 'edit' }
        format.json { render json: @books_store.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /books_stores/1
  # DELETE /books_stores/1.json
  def destroy
    @books_store.destroy
    respond_to do |format|
      format.html { redirect_to books_stores_url }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_books_store
      @books_store = BooksStore.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def books_store_params
      params.require(:books_store).permit(:book_id, :store_id)
    end
end
