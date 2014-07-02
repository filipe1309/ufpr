class CodesController < ApplicationController
  before_action :set_code, only: [:show, :edit, :update, :destroy]

  # GET /codes
  # GET /codes.json
  def index
    @codes = Code.all
  end

  # GET /codes/1
  # GET /codes/1.json
  def show
  end

  # GET /codes/new
  def new
    @code = Code.new
    @books = Book.all
	respond_to do |format|
		format.html
		format.xml { render :xml => @book}
	end
  end

  # GET /codes/1/edit
  def edit
  end

  # POST /codes
  # POST /codes.json
  def create
    @code = Code.new(code_params)

    respond_to do |format|
      if @code.save
        format.html { redirect_to @code, notice: 'Codigo adicionado.' }
        format.json { render action: 'show', status: :created, location: @code }
      else
        format.html { render action: 'new' }
        format.json { render json: @code.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /codes/1
  # PATCH/PUT /codes/1.json
  def update
    respond_to do |format|
      if @code.update(code_params)
        format.html { redirect_to @code, notice: 'Codigo atualizado.' }
        format.json { head :no_content }
      else
        format.html { render action: 'edit' }
        format.json { render json: @code.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /codes/1
  # DELETE /codes/1.json
  def destroy
    @code.destroy
    respond_to do |format|
      format.html { redirect_to codes_url }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_code
      @code = Code.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def code_params
      params.require(:code).permit(:name, :book_id)
    end
end
