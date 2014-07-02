class Book < ActiveRecord::Base
    has_many :editions , :dependent=>:destroy
    has_one :code , :dependent=>:destroy
    has_and_belongs_to_many :stores
end
